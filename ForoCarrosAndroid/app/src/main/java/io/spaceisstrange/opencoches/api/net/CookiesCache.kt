package io.spaceisstrange.opencoches.api.net

/**
 * This file is part of Nowbe for Android
 *
 * Copyright (c) 2016 The Nowbe Team
 * Maintained by Fran González <@spaceisstrange>
 */

class CookiesCache {
    companion object {
        /**
         * Variable donde se guardarán las cookies de la sesión actual
         */
        var cookies: Map<String, String>? = null
    }
}