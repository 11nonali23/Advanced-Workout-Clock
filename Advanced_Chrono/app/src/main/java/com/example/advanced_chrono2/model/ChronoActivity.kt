package com.example.advanced_chrono2.model

class ChronoActivity(val id: Int, val name: String, private val timings: ArrayList<Long>?)
{
    override fun toString(): String
    {
        return name
    }

    override fun equals(other: Any?): Boolean
    {
        if (other is ChronoActivity)
            return this.id == other.id

        return false
    }

    override fun hashCode(): Int
    {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}