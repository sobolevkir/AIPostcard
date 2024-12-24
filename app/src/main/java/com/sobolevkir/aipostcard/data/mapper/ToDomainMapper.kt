package com.sobolevkir.aipostcard.data.mapper

interface ToDomainMapper<I, O> {

    fun toDomain(dto: I): O
}