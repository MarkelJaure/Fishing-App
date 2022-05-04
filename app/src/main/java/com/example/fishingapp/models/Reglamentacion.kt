package com.example.fishingapp.models


data class Reglamentacion (
            var nombre: String,
            var descripcion: String,
            var ubicacion: String,
)
{

        companion object {
            val data
                get() = listOf(
                    Reglamentacion(
                        "Señuelos autorizados",
                        "La pesca debe practicarse con un señuelo artificial con un único anzuelo (simple, doble o triple). Cuando se quiera utilizar un señuelo que tenga más de un anzuelo, deben quitarse los restantes o inutilizarlos de forma tal que no puedan clavarse en los peces. En los ambientes de devolución obligatoria solo se permite la utilización de un anzuelo simple, sin rebaba o con rebaba aplastada. Se prohíbe el uso de señuelos que contengan pilas o baterías.",
                        "Puerto Madryn",
                    ),
                    Reglamentacion(
                        "Embarcación",
                        "Todas las embarcaciones usadas con fines de pesca deben cumplir los requisitos exigidos por Prefectura Naval Argentina.",
                        "Bahia Blanca",
                    ),
                    Reglamentacion(
                        "Número máximo de piezas por pescador",
                        "Para el caso de ambientes que no figuren en el listado alfabético con reglamentación específica correspondiente, se permitirá el sacrificio de un (1) salmónido, dos (2) Percas y cuatro (4) ejemplares de Pejerrey por día y por pescador, con la excepción de los ríos y arroyos donde Chubut adhiere al Reglamento General donde se establece la Devolución Obligatoria. En las áreas de devolución obligatoria se prohíbe la tenencia de ejemplares de peces de cualquier origen.",
                        "Lago Puelo",
                    )
                )
        }
}