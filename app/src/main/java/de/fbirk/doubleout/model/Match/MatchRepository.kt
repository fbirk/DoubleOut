package de.fbirk.doubleout.model.Match

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class MatchRepository constructor(private val matchDao: MatchDao) {

    fun getMatchById(number: Int): LiveData<Match> {
        return matchDao.loadById(number)
    }

}