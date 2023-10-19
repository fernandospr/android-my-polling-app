package com.github.fernandospr.mypollingapplication

class PersonRepository(private val api: PersonApi) {
    suspend fun postPerson(person: Person) = api.postPerson(person)

    suspend fun getPostPersonStatus(code: String) = api.getPostPersonStatus(code)
}