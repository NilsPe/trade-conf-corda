package com.r3.developers.utxodemo.states

import com.r3.developers.utxodemo.contracts.TestContract
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey

@BelongsToContract(TestContract::class)
class TestUtxoState(
    val testField: String,
    override val participants: List<PublicKey>
) : ContractState