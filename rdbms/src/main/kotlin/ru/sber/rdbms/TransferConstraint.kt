package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {
    val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5433/db",
            "postgres",
            "admin"
    )

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connection.use { connection ->
            try {
                val takeMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = amount - $amount WHERE id = $accountId1"
                )
                takeMoneyStatement.use { st ->
                    st.executeUpdate()
                }
                val giveMoneyStatement = connection.prepareStatement(
                    "UPDATE account1 SET amount = amount + $amount WHERE id = $accountId2"
                )
                giveMoneyStatement.use { st ->
                    st.executeUpdate()
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}
