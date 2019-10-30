package ar.edu.utn.frba.mobile.smarket.enums

import ar.edu.utn.frba.mobile.smarket.R

enum class PurchaseStatus(val value: Int, val color: Int) {
    FINISHED(R.string.status_finished, R.color.colorGreen), PENDING(R.string.status_pending, R.color.colorOrange), ON_ROUTE(R.string.status_on_route, R.color.colorYellow)
}