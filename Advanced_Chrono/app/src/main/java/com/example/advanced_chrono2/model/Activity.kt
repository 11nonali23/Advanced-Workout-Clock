package com.example.advanced_chrono2.model

open class Activity (val id: Int, val name: String)
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