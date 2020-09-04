package com.example.fishingpro.data.source.remote.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.FishData
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.source.FishSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FishRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) : FishSource {

    //Callbackflow is a good way to emit values inside listeners
    override fun retrieveCatches(userId: String): Flow<Result<List<LocalDailyCatch?>>> = callbackFlow {
        offer(Result.Loading)
        try {
            //Listen for every change in firebase db
            val request = firestore.collection("Catch").whereEqualTo("UserId", userId)
                .addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }
                value?.let {
                    val dataMapped = it.documents.map { doc ->
                        val singleCatch = doc.toObject(LocalCatch::class.java)
                        singleCatch?.CatchId = doc.id
                        singleCatch
                    }

                    //Create a map grouped by the date
                    val groupByDate = dataMapped.groupBy { data -> data?.Date }

                    val localDailyCatch = mutableListOf<LocalDailyCatch>()
                    groupByDate.forEach { (date, listCatches) ->
                        localDailyCatch.add(
                            LocalDailyCatch(
                                userId = userId,
                                date = date ?: Date(),
                                fish = listCatches.map { catch ->
                                    catch?.let { fish ->
                                        FishData(
                                            fishId = fish.FishId,
                                            location = fish.Location,
                                            weight = fish.Weight
                                        )
                                    }
                                }
                            )
                        )
                    }
                    offer(Result.Success(localDailyCatch))
                    channel.close()
                }
            }
            //Finally if collect is not in use or collecting any data we cancel this channel to prevent any leak and remove the subscription listener to the database
            awaitClose { request.remove() }
        } catch (e: FirebaseFirestoreException) {
            offer(Result.ExError(e))
        }
    }
}