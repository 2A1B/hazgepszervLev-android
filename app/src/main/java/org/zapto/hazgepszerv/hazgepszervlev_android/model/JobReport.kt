package org.zapto.hazgepszerv.hazgepszervlev_android.model

class JobReport(val customer_name : String,
                val customer_address : String,
                val customer_phone : String,
                val planned_delivery: String,
                val problem_review : String,
                val job_title : String,
                val status : String,
                val uuid : String) {

    constructor() : this("","","","","","","","")
}