package ru.sber.rdbms

import java.sql.DriverManager

class TransferOptimisticLock {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/db",
        "postgres",
        "admin"
    )
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { connection ->
            val autoCommit = connection.autoCommit
            try {
                connection.autoCommit = false
                val statementAccount1 = connection.prepareStatement(
                    "SELECT amount, version FROM account1 WHERE id = $accountId1"
                )
                val account1: AccountData
                statementAccount1.use { st ->
                    st.executeQuery().use {
                        it.next()
                        account1 = AccountData(accountId1, it.getLong("amount"), it.getLong("version"))
                    }
                }
                val statementAccount2 = connection.prepareStatement(
                    "SELECT amount, version FROM account1 WHERE id = $accountId2"
                )
                val account2: AccountData
                statementAccount2.use { st ->
                    st.executeQuery().use {
                        it.next()
                        account2 = AccountData(accountId2, it.getLong("amount"), it.getLong("version"))
                    }
                }
                if(account1.amount < amount)
                    throw DBException("Not enough money to perform operation")
                val takeMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = ${account1.amount - amount}, " +
                            "version = ${account1.version + 1} WHERE id = $accountId1 AND version = ${account1.version}"
                )

                val giveMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = ${account2.amount + amount}, " +
                            "version = ${account2.version + 1} WHERE id = $accountId2 AND version = ${account2.version}"
                )
                takeMoneyStatement.use { st ->
                    st.executeUpdate()
                    if(st.updateCount == 0)
                        throw DBException("The row was modified")
                }
                giveMoneyStatement.use { st ->
                    st.executeUpdate()
                    if(st.updateCount == 0)
                        throw DBException("The row was modified")
                }
                connection.commit()
            } catch (ex: Exception) {
                println(ex.message)
                connection.rollback()
            } finally {
                connection.autoCommit = autoCommit
            }
        }
    }
}

data class AccountData(val id: Long, val amount: Long, val version: Long)
