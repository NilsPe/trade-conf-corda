package com.r3.developers.demo.tradeconf.states

import com.r3.developers.demo.tradeconf.contracts.TradeConfContract
import net.corda.v5.application.membership.MemberLookup
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.base.types.MemberX500Name
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey
import java.util.*

@BelongsToContract(TradeConfContract::class)
data class TradeConfState(
    val tradeId : String,
    val details: String,
    val lastUpdateBy: MemberX500Name,
    val status: String,
    // The participants, represented by their public key.
    private val participants: List<PublicKey>
) : ContractState {

    override fun getParticipants(): List<PublicKey> {
        return participants
    }
}

data class TradeConfDTO(
    val tradeId : String,
    val details: String,
    val lastUpdateBy: String,
    val status: String,
    // The participants, NOW represented by their X500name.
    val participants: List<String>,
)

@Suspendable
fun TradeConfState.toTradeConfDTO(memberLookup: MemberLookup) = TradeConfDTO(
    tradeId = tradeId,
    details = details,
    lastUpdateBy = lastUpdateBy.toString(),
    status = status,
    participants = participants.map { memberLookup.lookup(it)?.name.toString() }
)