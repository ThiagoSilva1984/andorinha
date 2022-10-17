package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
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
import model.seletor.TweetSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestTweetRepository {

	private static final int ID_TWEET_CONSULTA = 2;
	private static final int ID_USUARIO_CONSULTA = 1;

	private static final Long DELTA_MILIS = (long) 500;

	@EJB
	private UsuarioRepository usuarioRepository;

	@EJB
	private TweetRepository tweetRepository;

	@Before
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	// Teste de inserção de novo tweet
	@Test
	public void testa_se_tweet_foi_inserido() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {

		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA); // id = 1

		Tweet tweet = new Tweet();
		tweet.setConteudo("Teste de Inserção de tweet.");
		tweet.setUsuario(usuario);

		this.tweetRepository.inserir(tweet);

		assertThat(tweet.getId()).isGreaterThan(0);

		Tweet inserido = this.tweetRepository.consultar(tweet.getId());

		assertThat(inserido).isNotNull();
		assertThat(inserido.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getDataPostagem().getTime(), DELTA_MILIS);

		System.out.println(tweet);
	}

	// Dado o id de um tweet, precisa consultar se existe e trazer os dados
	@Test
	public void testa_consultar_tweet() {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA); // id = 2

		assertThat(tweet).isNotNull();
		assertThat(tweet.getConteudo()).isNotNull();
		assertThat(tweet.getId()).isEqualTo(ID_TWEET_CONSULTA);
		assertThat(tweet.getUsuario()).isNotNull();

		System.out.println(tweet);
	}

	// Teste vai consultar um tweet e vai alterar ele
	@Test
	public void testa_alterar_tweet() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA); // id = 2
		tweet.setConteudo("Postagem Atualizada/Alterada");

		this.tweetRepository.atualizar(tweet);

		Tweet alterado = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		assertThat(alterado.getConteudo()).isNotNull();
		assertThat(alterado.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getDataPostagem().getTime(), DELTA_MILIS);

		System.out.println(tweet);
	}

	// Dado o id de um tweet, remove-lo
	@Test
	public void testa_remover_tweet() {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		assertThat(tweet).isNotNull();

		this.tweetRepository.remover(ID_TWEET_CONSULTA);

		Tweet tweetRemovido = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		assertThat(tweetRemovido).isNull();

		System.out.println(tweetRemovido);
	}

	// Buscar todos os tweets e guardar em uma lista de tweets
	@Test
	public void testa_listar_todos_tweets_de_usuario() {
		List<Tweet> tweets = this.tweetRepository.listarTodos();
		assertThat(tweets).isNotNull();

		assertThat(tweets).isNotNull().isNotEmpty().hasSize(3).extracting("conteudo").containsExactlyInAnyOrder(
				"Minha postagem de teste 1", "Minha postagem de teste 2", "Minha postagem de teste 3");

		tweets.stream().forEach(t -> {
			assertThat(t.getDataPostagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			System.out.println(t);
		});
	}

	// Pesquisar tweets com uma palavra
	@Test
	public void pesquisar_tweet_por_palavra() {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setConteudo("Minha ");

		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);

		assertThat(tweets).isNotNull();
		assertThat(tweets.size()).isEqualTo(3);
		tweets.stream().forEach(t -> {
			assertThat(t.getId()).isNotNull();
			assertThat(t.getConteudo()).isNotNull();
			assertThat(t.getDataPostagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			System.out.println(t);
		});
	}

	// Pesquisar tweets por id de tweet
	@Test
	public void pesquisar_tweet_por_id_de_tweet() {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setId(1);

		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);

		assertThat(tweets).isNotNull();
		assertThat(tweets.size()).isEqualTo(1);
		tweets.stream().forEach(t -> {
			assertThat(t.getId()).isNotNull();
			assertThat(t.getConteudo()).isNotNull();
			assertThat(t.getDataPostagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			System.out.println(t);
		});
	}

	// Pesquisar tweets por data de postagem
	@Test
	public void pesquisar_tweet_por_data_de_postagem() {
		Tweet tweet = this.tweetRepository.consultar(1);

		TweetSeletor seletor = new TweetSeletor();
		seletor.setDataPostagemInicial(tweet.getDataPostagem());

		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);

		assertThat(tweets).isNotNull();
		assertThat(tweets.size()).isEqualTo(1);
		tweets.stream().forEach(t -> {
			assertThat(t.getId()).isNotNull();
			assertThat(t.getConteudo()).isNotNull();
			assertThat(t.getDataPostagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			System.out.println(t);
		});
	}

	// Pesquisar tweets por data de postagem
	@Test
	public void pesquisar_tweet_por_periodo_de_postagem() {
		Tweet tweet = this.tweetRepository.consultar(2);
		Tweet tweet3 = this.tweetRepository.consultar(3);
		TweetSeletor seletor = new TweetSeletor();
		seletor.setDataPostagemInicial(tweet3.getDataPostagem());
		seletor.setDataPostagemFinal(tweet.getDataPostagem());

		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);

		assertThat(tweets).isNotNull();
		assertThat(tweets.size()).isEqualTo(3);
		tweets.stream().forEach(t -> {
			assertThat(t.getId()).isNotNull();
			assertThat(t.getConteudo()).isNotNull();
			assertThat(t.getDataPostagem()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			System.out.println(t);
		});
	}

	// Test para contar o total de tweets
	@Test
	public void testa_contar_total_de_tweets() {
		TweetSeletor seletor = new TweetSeletor();

		long totalTweets = this.tweetRepository.contar(seletor);

		System.out.println(totalTweets);

		assertThat(totalTweets).isNotNull();
		assertThat(totalTweets).isEqualByComparingTo((long) 3);

	}

	// Teste para contar o total de tweets por id
	@Test
	public void testa_contar_total_de_tweets_por_id() {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setId(ID_TWEET_CONSULTA);
		seletor.setId(seletor.getId());

		long totalTweets = this.tweetRepository.contar(seletor);

		System.out.println(totalTweets);

		assertThat(totalTweets).isNotNull();
		assertThat(totalTweets).isEqualByComparingTo((long) 1);
	}

}
