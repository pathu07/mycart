package com.cart.app

import com.cart.app.utils.AppUtils
import org.junit.Test

import org.junit.Assert.*


/**
 * Created by padmanabhan on 2/14/18.
 */

class AppUtilUnitTest {

    @Test
    fun check_dollar(){

        val priceString = 2.0
        val result = AppUtils.addDollar(priceString)
        assertTrue(result == "$2.0")
    }


    @Test
    fun checkRoundValue(){
        val doubleValue = 12.3333333333
        val resultVal = AppUtils.round(doubleValue, 2)
        assertTrue(resultVal == 12.33)

        val resultVal2 = AppUtils.round(doubleValue, 3)
        assertTrue(resultVal2 == 12.333)
    }
}
