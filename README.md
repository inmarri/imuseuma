# imuseuma
iMuseumA, una solución de museos inteligentes.

iMuseumA es un sistema para hacer inteligente el espacio museístico, está basado en el paradigma de los agentes software y como tal está compuesto por los agentes (el software autonomo y distribuido) y la plataforma (el middleware que hace posible la interacción entre los agentes). 

La tecnología de agentes usada por iMuseumA es Self-StarMAS [1], una familia de agentes auto-configurables, cuyas principales características son la comunicación basada en el estándar FIPA (http://www.fipa.org/) y el hecho de que pueden ser deplegados en distintas plataformas de agentes como Jade (https://jade.tilab.com/) o Sol , o incluso usando distintas tecnologías de comunicaciones como Ethernet, WiFi, ZigBee o Bluetooth. Además esto agentes pueden ser desplegados en distintos tipos de dispositivos, en el caso de iMuseumA se desplegaron en dispositivos Android, motas sensoras Waspmote y SunSPOT.

Como ya se ha mencionado, la plataforma utilizada por iMuseumA es Sol, una plataforma de agentes para la Internet de las Cosas (IoT, por sus siglas en Inglés Internet of Things). Esta plataforma permite que agentes con tecnologías de comunicación diversa puedan interactuar. Además, a través de sus clientes da soporte a dispositivos con comunicaciones de bajo alcance como ZigBee y Bluetooth. En el caso del Museo se desplegó un cliente en el router Multi-Protocolo Meshlium (http://www.libelium.com/products/meshlium/).

A la hora de desplegar iMuseumA seguiremos los siguientes pasos:
1. Poner en ejecución la plataforma de agentes.
2. Desplegar clientes de la plataforma.
3. Desplegar los distintos tipos de agentes.

# referencias
[1] Inmaculada Ayala, Mercedes Amor, and Lidia Fuentes. 2013. Self-configuring agents for ambient assisted living applications. Personal Ubiquitous Comput. 17, 6 (August 2013), 1159-1169. DOI=http://dx.doi.org/10.1007/s00779-012-0555-9

[2] Inmaculada Ayala, Mercedes Amor, and Lidia Fuentes. 2015. The Sol agent platform: Enabling group communication and interoperability of self-configuring agents in the Internet of Things. J. Ambient Intell. Smart Environ. 7, 2 (March 2015), 243-269.
