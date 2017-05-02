# Requisitos de la aplicación
* **RF001**: Existirá un vendedor que posee uno o más libros a la venta
* **RF002**: Los compradores deben implementar un precio máximo que están dispuestos a pagar
* **RF003**: Los vendedores deben implementar el paso o incremento entre dos pujas sucesivas (los compradores no pujan, simplemente dicen si están interesados o no en cada ronda).
* **RF004**: Entre dos incrementos sucesivos deberán transcurrir 10 segundos
* **RF005**: La subasta concluirá si en una ronda ningún comprador puja, en cuyo caso se asignará a un coprador aleatorio de la ronda anterior.
* **RF006**: La subasta concluirá si en una ronda existe un único comprador interesado.
* **RF007**: El número de compradores podrá variar dinámicamente durante la subasta.
* **RF008**: Se podrán mantener múltiples subastas simultáneamente (una subasta por libro)
* **RF009**: El vendedor contará con interfaz gráfica para iniciar la subasta de nuevos libros y realizar seguimiento de subastas activas. Los parámetros son: `nombreLibro`, `precioInicial`, `incremento`
* **RF010**: El comprador contará con interfaz gráfica que ilustre el estado de las subastas en las que esté interesado.