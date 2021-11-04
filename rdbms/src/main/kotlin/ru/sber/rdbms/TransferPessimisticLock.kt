package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
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
                    "SELECT * FROM account1 WHERE id = $accountId1"
                )
                statementAccount1.executeQuery().use { st ->
                    st.next()
                    if(st.getLong("amount") < amount)
                        throw DBException("Not enough money to perform operation")
                }
                val prepareStatement = connection.prepareStatement(
                    "SELECT * FROM account1 WHERE id in ($accountId1, $accountId2) FOR update"
                )
                prepareStatement.use {
                    it.executeQuery()
                }
                val takeMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = amount - $amount WHERE id = $accountId1"
                )
                takeMoneyStatement.use {
                    it.executeUpdate()
                }
                val giveMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = amount + $amount WHERE id = $accountId2"
                )
                giveMoneyStatement.use {
                    it.executeUpdate()
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
