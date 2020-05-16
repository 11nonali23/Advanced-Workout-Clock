package com.example.advanced_chrono2.model

open class Activity (val id: Int, val name: String)
{
    override fun toString(): String
    {
        return name
    }

    override fun equals(other: Any?): Boolean
    {
        if (other is ChronometerActivity && this is ChronometerActivity)
            return this.id == other.id

        if(other is TimerActivity && this is TimerActivity)
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