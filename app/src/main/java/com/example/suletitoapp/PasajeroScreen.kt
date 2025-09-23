package com.example.suletitoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.RowScope
import androidx.compose.ui.draw.clip
import java.util.Locale

@Composable
fun PasajeroScreen(
    nombre: String,
    saldo: Double,
    onCerrarSesion: () -> Unit,
    onRecargarSaldo: () -> Unit,
    onPagarNFC: () -> Unit,
    onVerHistorial: () -> Unit,
    // opcionales y retrocompatibles:
    onRecargarConMonto: ((Double) -> Unit)? = null,
    ultimoMovimiento: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER: avatar + saludo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                val inicial = nombre.firstOrNull()?.uppercaseChar() ?: 'U'
                Text(
                    text = inicial.toString(),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Hola, $nombre",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "¡Qué bueno verte!",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent) // usamos el box interno para el fondo
        ) {
            // interior con gradiente
            val gradient = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradient)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Saldo",
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Saldo disponible",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = String.format(Locale.getDefault(), "Bs. %.2f", saldo),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (!ultimoMovimiento.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Último movimiento: $ultimoMovimiento",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // BOTON PRINCIPAL - Pagar con NFC
        ElevatedButton(
            onClick = { onPagarNFC() },
            modifier = Modifier
                .fillMaxWidth(),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Payment,
                contentDescription = "Pagar NFC",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pagar con Sueltito",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // BOTON SECUNDARIO - Recargar saldo (con icono)
        OutlinedButton(
            onClick = { onRecargarSaldo() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Recargar",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Recargar saldo", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "",
            modifier = Modifier.height(4.dp)
        ) // separación pequeña

        OutlinedButton(
            onClick = { onVerHistorial() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Historial",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ver historial", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTÓN DE CERRAR SESIÓN - menos prominente, color de error
        OutlinedButton(
            onClick = { onCerrarSesion() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Cerrar sesión", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

// componente auxiliar para chips rápidos
@Composable
private fun RowScope.QuickRechargeChip(label: String, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(text = label) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        },
        modifier = Modifier,
        colors = AssistChipDefaults.assistChipColors()
    )
}
