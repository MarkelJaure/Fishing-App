package com.example.proyectovacio

import android.graphics.Bitmap

class Concurso{
    data class Concurso (
        var nombre: String,
        var basesAndCondiciones: String,
        var premio: String,
        var ranking: List<Report.Reporte>?,
    )

    companion object {
        val data
            get() = listOf(
                Concurso(
                    "Nombre concurso 1",
                    "La participación en esta Promoción “FLORECE CON QIDA” implica el conocimiento y la\n" +
                            "aceptación por parte del participante de las presentes bases y condiciones.\n" +
                            "Cualquier violación a las mismas o a los procedimientos o sistemas aquí\n" +
                            "establecidos para la realización del presente concurso implicará la inmediata\n" +
                            "exclusión de las mismas y/o la revocación de los premios.-",
                    "nada de nada",
                    Report.Companion.data
                ),
                Concurso(
                    "Nombre concurso 2",
                    "Viaje a un viaje para 2 personas a la ciudad de Mar del Plata. Duración del\n" +
                            "viaje: 6 días / 5 noches en la ciudad de Mar del Plata entre el 1/3/2018 y el\n" +
                            "15/4/2018. Incluye alojamiento en hotel 3 estrellas o superior y pensión completa.\n" +
                            "El hotel será elegido y designado por Empresur S.A. Traslado en avión para 2\n" +
                            "personas en clase económica; el horario y la empresa a realizar el servicio será\n" +
                            "elegida y designada por Empresur S.A. El punto de partida del viaje será la\n" +
                            "localidad de origen de la sucursal del cliente y el destino será la ciudad Mar del\n" +
                            "Plata. Una vez entregado el premio, el EMPRESUR S.A. quedará liberado de toda\n" +
                            "responsabilidad por el mismo. El viaje no podrá ser transferido; ni canjeado por\n" +
                            "dinero ni ofertas. El PREMIO no incluye: cualquier concepto no especificado en las\n" +
                            "presentes bases y condiciones como por ejemplo otros gastos de hotel tales como\n" +
                            "el uso del teléfono, fax, servicio de habitación, bar de la habitación, películas\n" +
                            "del hotel y otras facilidades del hotel; comidas y bebidas, u otros costos y gastos\n" +
                            "de cualquier tipo para el ganador y acompañante del PREMIO ; dinero en efectivo,\n" +
                            "impuestos gubernamentales, tasas aeroportuarias, y otros cargos personales que\n" +
                            "tenga durante su estadía el ganador del PREMIO.",
                    "Un trofeito",
                    null

                ),
                Concurso(
                    "Nombre concurso 3",
                    "100 cajas navideñas que contendrán cada una: 1 Donuts Leche Bonafide x 78\n" +
                            "gramos, Rama Leche Bonafide 80 gramos, Postre Nugaton por 110 gramos, Nugaton\n" +
                            "Blanco Bonafide DDL 3 x 27 gramos, Pan Dulce con Chips de Chocolate x 480 gramos,\n" +
                            "Turrón Maní Bonafide con Miel Cremona x 100 gramos, Garrapiñada Bonafide x 100\n" +
                            "gramos, Budín de Limón Bonafide x 250 gramos, Vizzio Maní Bonafide x 80 gramos,\n" +
                            "Vizzio Bonafide Cereal x 72 gramos, una sidra real y un maletín Bonafide diseño\n" +
                            "fiestas. ",
                    "Una medallita",
                    null
                )
            )
    }}