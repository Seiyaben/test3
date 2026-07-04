package com.foretmagique.content.tracing

// x and y range 0..1 within a letter's bounding box, independent of any
// on-screen pixel size so the geometry can be unit-tested without Android.
data class NormalizedPoint(val x: Float, val y: Float)
