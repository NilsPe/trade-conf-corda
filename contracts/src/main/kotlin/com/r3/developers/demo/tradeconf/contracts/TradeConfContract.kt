package com.r3.developers.demo.tradeconf.contracts

import com.r3.developers.demo.tradeconf.states.TradeConfState
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.ledger.utxo.Command
import net.corda.v5.ledger.utxo.Contract
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction

class TradeConfContract: Contract {

    // Command Class used to indicate that the transaction should start a new chat.
    class Share: Command
    // Command Class used to indicate that the transaction should append a new ChatState to an existing chat.
    class Update: Command

    // verify() function is used to apply contract rules to the transaction.
    override fun verify(transaction: UtxoLedgerTransaction) {

        // Ensures that there is only one command in the transaction
        val command = transaction.commands.singleOrNull() ?: throw CordaRuntimeException("Requires a single command.")

        // Applies a universal constraint (applies to all transactions irrespective of command)
        "The output state should have two and only two participants." using {
            val output = transaction.outputContractStates.first() as TradeConfState
            output.participants.size== 2
        }
        // Switches case based on the command
        when(command) {
            // Rules applied only to transactions with the Create Command.
            is Share -> {
                "When command is Create there should be no input states." using (transaction.inputContractStates.isEmpty())
                "When command is Create there should be one and only one output state." using (transaction.outputContractStates.size == 1)
            }
            // Rules applied only to transactions with the Update Command.
            is Update -> {
                "When command is Update there should be one and only one input state." using (transaction.inputContractStates.size == 1)
                "When command is Update there should be one and only one output state." using (transaction.outputContractStates.size == 1)

                val input = transaction.inputContractStates.single() as TradeConfState
                val output = transaction.outputContractStates.single() as TradeConfState
                "When command is Update id must not change." using (input.id == output.id)
                "When command is Update chatName must not change." using (input.details == output.details)
                "When command is Update participants must not change." using (
                        input.participants.toSet().intersect(output.participants.toSet()).size == 2)
            }
            else -> {
                throw CordaRuntimeException("Command not allowed.")
            }
        }
    }

    // Helper function to allow writing constraints in the Corda 4 '"text" using (boolean)' style
    private infix fun String.using(expr: Boolean) {
        if (!expr) throw CordaRuntimeException("Failed requirement: $this")
    }

    // Helper function to allow writing constraints in '"text" using {lambda}' style where the last expression
    // in the lambda is a boolean.
    private infix fun String.using(expr: () -> Boolean) {
        if (!expr.invoke()) throw CordaRuntimeException("Failed requirement: $this")
    }
}