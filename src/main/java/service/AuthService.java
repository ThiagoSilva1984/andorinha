package service;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Usuario;
import model.dto.AuthDTO;
import repository.JwtRepository;
import repository.UsuarioRepository;

@Path("/auth")
public class AuthService {
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	@Inject
	JwtRepository jwtRepository; 
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMAÇÃO EM QUE FORMATO?
	@Produces(MediaType.APPLICATION_JSON) // E eu vou produzir um texto, pq eu estou devolvendo um int, e não estou devolvendo um objeto
	public Response login(AuthDTO authDTO) {
		Usuario usuario = this.usuarioRepository.login(authDTO.getUsuario(), authDTO.getSenha());
		if( usuario != null ) {
			
			Calendar expiracao = Calendar.getInstance();
			expiracao.add(Calendar.HOUR_OF_DAY, 3);
			
			String jwt = this.jwtRepository.generateToken(usuario, expiracao.getTime());
			
			return Response.ok(usuario).header("x-token", jwt).build();
		}
		else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

}
