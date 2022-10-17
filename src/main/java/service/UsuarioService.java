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

import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import model.seletor.UsuarioSeletor;
import repository.UsuarioRepository;

@Path("/usuario") //com isso, para acessar aqui tenho que colocar /api/usuario
public class UsuarioService {
	
	/* como transformo esse m�todo, em m�todo que realmente vai responder a uma chamada rest? 
	 * 1� - tenho que colocar qual � o m�todo http que vai ser atendido por esse m�todo
	 * m�todos que atendem: GET POST DELETE PATCH OPTIONS
	 * 2� Preciso colocar o que o m�todo produz
	 */
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	//LISTAR TODOS OS USU�RIOS
	@GET
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Usuario> listarTodos(){
		return this.usuarioRepository.listarTodos();
	}
	
	//INSERIR USU�RIO
	@POST
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMA��O EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e n�o estou devolvendo um objeto
	public int inserir(Usuario usuario) {
		this.usuarioRepository.inserir(usuario);
		return usuario.getId();
	}
	
	//CONSULTAR USU�RIO
	@GET
	@Path("/{id}") //TUDO QUE EU COLOCAR CHAVE NO path, � um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario consultar( @PathParam("id") Integer id) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		return this.usuarioRepository.consultar(id);
	}
	
	//ATUALIZAR USU�RIO
	@PUT
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMA��O EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e n�o estou devolvendo um objeto
	public void atualizar(Usuario usuario) {
		this.usuarioRepository.atualizar(usuario);
	}
	
	//DELETAR USU�RIO
	@DELETE
	@Path("/{id}") //TUDO QUE EU COLOCAR CHAVE NO path, � um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public void remover( @PathParam("id") Integer id) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		this.usuarioRepository.remover(id);
	}
	
	//PESQUISAR USU�RIO
	@POST
	@Path("/pesquisar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Usuario> pesquisar( UsuarioSeletor seletor ){
		return this.usuarioRepository.pesquisar(seletor);
	}
	

}
