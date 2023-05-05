package com.r3.developers.demo.tradeconf.states

import com.r3.developers.demo.tradeconf.contracts.TradeConfContract
import net.corda.v5.base.types.MemberX500Name
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey
import java.util.*

@BelongsToContract(TradeConfContract::class)
data class TradeConfState(
    val id : String,
    val details: String,
    val sharedBy: MemberX500Name,
    val status: String,
    // The participants, represented by their public key.
    private val participants: List<PublicKey>) : ContractState {

    override fun getParticipants(): List<PublicKey> {
        return participants
    }

    // Helper function to create a new ChatState from the previous (input) ChatState.
    fun updateMessage(messageFrom: MemberX500Name, message: String) =
        copy(sharedBy = messageFrom, status = message)
}

