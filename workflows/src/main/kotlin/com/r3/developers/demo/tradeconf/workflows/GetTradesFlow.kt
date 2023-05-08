package com.r3.developers.demo.tradeconf.workflows

import com.r3.developers.demo.tradeconf.states.TradeConfState
import com.r3.developers.demo.tradeconf.states.toTradeConfDTO
import net.corda.v5.application.flows.ClientRequestBody
import net.corda.v5.application.flows.ClientStartableFlow
import net.corda.v5.application.flows.CordaInject
import net.corda.v5.application.marshalling.JsonMarshallingService
import net.corda.v5.application.membership.MemberLookup
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.utxo.UtxoLedgerService
import org.slf4j.LoggerFactory

// See Chat CorDapp Design section of the getting started docs for a description of this flow.
class GetTradesFlow : ClientStartableFlow {

    private companion object {
        val log = LoggerFactory.getLogger(this::class.java.enclosingClass)
    }

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    lateinit var ledgerService: UtxoLedgerService

    @CordaInject
    lateinit var memberLookup: MemberLookup

    @Suspendable
    override fun call(requestBody: ClientRequestBody) : String {

        log.info("ListChatsFlow.call() called")

        // Queries the VNode's vault for unconsumed states and converts the result to a serializable DTO.
        val states = ledgerService.findUnconsumedStatesByType(TradeConfState::class.java)

        val result = states.map { it.state.contractState.toTradeConfDTO(memberLookup) }

        // Uses the JsonMarshallingService's format() function to serialize the DTO to Json.
        return jsonMarshallingService.format(result);
    }
}
