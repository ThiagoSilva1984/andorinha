package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Comentario;
import model.dto.ComentarioDTO;
import model.seletor.ComentarioSeletor;
import repository.ComentarioRepository;

@Path("/comentario") //com isso, para acessar aqui tenho que colocar /api/usuario
public class ComentarioService {
	
	@EJB
	ComentarioRepository comentarioRepository;
	
	//LISTAR TODOS OS COMETÁRIOS
	@GET
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Comentario> listarTodos(){
		return this.comentarioRepository.listarTodos();
	}
	
	//INSERIR COMENTARIO
	@POST
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMAÇÃO EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e não estou devolvendo um objeto
	public int inserir(Comentario comentario) {
		this.comentarioRepository.inserir(comentario);
		return comentario.getId();
	}
	
	// CONSULTAR COMENTÁIO
	@GET
	@Path("/{idComentario}") //TUDO QUE EU COLOCAR CHAVE NO path, é um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public Comentario consultar( @PathParam("idComentario") Integer idComentario) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		return this.comentarioRepository.consultar(idComentario);
	}
	
	//ATUALIZAR
	@PUT
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMAÇÃO EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e não estou devolvendo um objeto
	public void atualizar(Comentario comentario) {
		this.comentarioRepository.atualizar(comentario);
	}
	
	//DELETAR
	@DELETE
	@Path("/{id}") //TUDO QUE EU COLOCAR CHAVE NO path, é um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public void remover( @PathParam("id") Integer id) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		this.comentarioRepository.remover(id);
	}
	
	// PESQUISAR
	@POST
	@Path("/pesquisar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Comentario> pesquisar( ComentarioSeletor seletor ){
		return this.comentarioRepository.pesquisar(seletor);
	}
	
//	PESQUISAR DTO
	@POST
	@Path("/dto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<ComentarioDTO> pesquisarDTO( ComentarioSeletor seletor ){
		return this.comentarioRepository.pesquisarDTO(seletor);
	}

}
