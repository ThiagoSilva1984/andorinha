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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import model.Tweet;
import model.Usuario;
import model.dto.TweetDTO;
import model.seletor.TweetSeletor;
import repository.TweetRepository;

@Path("/tweet") //com isso, para acessar aqui tenho que colocar /api/usuario
public class TweetService {
	
	@EJB
	TweetRepository tweetRepository;
	
	@Context
	private SecurityContext context;
	
	//LISTAR TWEETS
	@GET
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Tweet> listarTodos(){
		return this.tweetRepository.listarTodos();
	}
	
	//INSERIR TWEET
	@POST
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMAÇÃO EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e não estou devolvendo um objeto
	public int inserir(Tweet tweet) {
		tweet.setUsuario( (Usuario) this.context.getUserPrincipal() );
		
		this.tweetRepository.inserir(tweet);
		return tweet.getId();
	}
	
	//CONSULTAR TWEET
	@GET
	@Path("/{idTweet}") //TUDO QUE EU COLOCAR CHAVE NO path, é um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public Tweet consultar( @PathParam("idTweet") Integer idTweet) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		return this.tweetRepository.consultar(idTweet);
	}
	
	//ATUALIZAR TWEET
	@PUT
	@Consumes(MediaType.APPLICATION_JSON) // aqui estou dizendo: EU VOU RECEBER INFORMAÇÃO EM QUE FORMATO?
	@Produces(MediaType.TEXT_PLAIN) // E eu vou produzir um texto, pq eu estou devolvendo um int, e não estou devolvendo um objeto
	public void atualizar(Tweet tweet) {
		this.tweetRepository.atualizar(tweet);
	}
	
	//DELETAR TWEET
	@DELETE
	@Path("/{id}") //TUDO QUE EU COLOCAR CHAVE NO path, é um parametro
	@Produces(MediaType.APPLICATION_JSON)
	public void remover( @PathParam("id") Integer id) { //com o PathParam, agora tudo que for vindo pelo @Path("/{id}") vai parar aqui
		this.tweetRepository.remover(id);
	}
	
	// PESQUISAR TWEET
	@POST
	@Path("/pesquisar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<Tweet> pesquisar( TweetSeletor seletor ){
		return this.tweetRepository.pesquisar(seletor);
	}
	
//	PESQUISAR TWEET-DTO
	@POST
	@Path("/dto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) //sempre fazer o Produces
	public List<TweetDTO> pesquisarDTO( TweetSeletor seletor ){
		return this.tweetRepository.pesquisarDTO(seletor);
	}
	

}
