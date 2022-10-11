package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestTweetRepository {
	
	private static final int ID_TWEET_CONSULTA = 2;
	private static final int ID_USUARIO_CONSULTA = 1;
	private static final String POSTAGEM_ATUALIZADA_TWEET = "Postagem Atualizada";
		
	private static final Long DELTA_MILIS = (long) 500;
	
	@EJB
	private UsuarioRepository usuarioRepository;
	
	@EJB
	private TweetRepository tweetRepository;

	@Before 
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}
	
	@Test
	public void testa_se_tweet_foi_inserido() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		
		Tweet tweet = new Tweet();
		tweet.setConteudo(POSTAGEM_ATUALIZADA_TWEET);
		tweet.setUsuario(usuario);
		
		this.tweetRepository.inserir(tweet);
	
		assertThat( tweet.getId() ).isGreaterThan(0);
		
		Tweet inserido = this.tweetRepository.consultar(tweet.getId());
 		
		assertThat( inserido ).isNotNull();
		assertThat( inserido.getConteudo() ).isEqualTo( tweet.getConteudo() );
		assertThat( Calendar.getInstance().getTime() ).isCloseTo(inserido.getData_postagem().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_consultar_tweet() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
				
		assertThat( tweet ).isNotNull();
		assertThat( tweet.getConteudo() ).isNotNull();
		assertThat( tweet.getId() ).isEqualTo(ID_TWEET_CONSULTA);
		assertThat( tweet.getUsuario() ).isNotNull();
	}

	@Test
	public void testa_alterar_tweet() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		tweet.setConteudo("Postagem Atualizada/Alterada");

		this.tweetRepository.atualizar(tweet);

		Tweet alterado = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		assertThat(alterado.getConteudo()).isNotNull();
		assertThat(alterado.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getData_postagem().getTime(), DELTA_MILIS);
	}
	
	@Test
	public void testa_remover_tweet() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		assertThat(tweet).isNotNull();

		this.tweetRepository.remover(ID_TWEET_CONSULTA);

		Tweet tweetRemovido = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		assertThat(tweetRemovido).isNull();
	}
	
	@Test
	public void testa_listar_todos_tweets_de_usuario()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		List<Tweet> tweets = this.tweetRepository.listarTodos();
		assertThat(tweets).isNotNull();

		assertThat(tweets).isNotNull()
			.isNotEmpty()
			.hasSize(3)
			.extracting("conteudo")
			.containsExactlyInAnyOrder(
				"Minha postagem de teste"
				, "Minha postagem de teste 2"
				, "Minha postagem de teste 3");

		tweets.stream().forEach(t -> {
			assertThat(t.getData_postagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
		});
	}
}
	
	

