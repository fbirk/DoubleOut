package de.fbirk.doubleout.model.Match

import kotlinx.coroutines.flow.Flow

class MatchRepository constructor(private val matchDao: MatchDao) {

    fun getMatchById(number: Int): Flow<Match> {
        return matchDao.loadById(number)
    }

}