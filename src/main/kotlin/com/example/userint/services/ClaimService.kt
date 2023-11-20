package com.example.userint.services

import com.example.userint.client.CoreClient
import com.example.userint.domain.entities.Claim
import com.example.userint.domain.entities.Trips
import com.example.userint.domain.model.*
import com.example.userint.domain.requests.ClaimDTO
import com.example.userint.domain.requests.ClaimReponse
import com.example.userint.domain.requests.ClosedTrip
import com.example.userint.domain.requests.toClaimReponse
import com.example.userint.repositories.sql.ClaimRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class ClaimService  {

    @Autowired
    private lateinit var claimRepository: ClaimRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    lateinit var coreClient: CoreClient

    @Autowired
    private lateinit var tripService: TripService

    @Autowired
    lateinit var messagingTemplate: SimpMessageSendingOperations


    @Transactional
    fun create(claim: ClaimDTO, userCode: UUID): ClaimReponse {
       val user = userService.getUser(userCode).let {
           val trip = tripService.getTripByCode(claim.tripCode)
           val claim =  claimRepository.save(
               Claim(
                   userId = it,
                   created_at = Instant.now(),
                   is_finished = false,
                   title = claim.title,
                   description = claim.description,
                   tripCode = claim.tripCode.toString(),
                   status = Status.NEW.name,
                   driverName = claim.driverName
               )
           )
           coreClient.sendPostRequest(
               EventNewTicket(
                   exchange = "new_user_tickets",
                   message = NewTicket(
                       idTicket = claim.id,
                       idSolicitante = it.id,
                       idReclamado = trip!!.driverId!!.idChofer.toLong(),
                       idViaje =  trip.id,
                       asunto = claim.title,
                       detalle = claim.description,
                       status = Status.NEW,
                       prioridad =  Prioridad.BAJA,
                       tipoUsuario = TipoUsuario.USUARIO
                   )
               )
           )
           return  claim.toClaimReponse()
        }
    }

    fun getClaims(userCode: UUID): List<Claim?> {
        return claimRepository.findByUserId_Code(userCode)
    }

    @Transactional
    fun updateClaim(event: UpdateTicket) {
        claimRepository.findById(event.idTicket).let {
            if(it.isPresent){
                it.get().id = event.idTicket
                it.get().updated_at = Instant.now()
                it.get().status = event.newStatus

                claimRepository.save(it.get())

                val novedad = "El ticket con ID ${event.idTicket} ha sido modificado"
                messagingTemplate.convertAndSend("/topic/novedad", novedad)
            }
        }
    }
}
