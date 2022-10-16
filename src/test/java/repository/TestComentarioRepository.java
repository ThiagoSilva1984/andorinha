package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import model.seletor.ComentarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestComentarioRepository {

	private static final int ID_TWEET_CONSULTA = 1;
	private static final int ID_COMENTARIO_CONSULTA = 1;
	private static final int ID_USUARIO_CONSULTA = 1;

	private static final long DELTA_MILIS = 500;

	@EJB
	private UsuarioRepository usuarioRepository;

	@EJB
	private TweetRepository tweetRepository;

	@EJB
	private ComentarioRepository comentarioRepository;

	@Before
	public void setup() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_se_comentario_foi_inserido() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usarioComenta = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		Tweet tweetComentado = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		Comentario comentario = new Comentario();
		comentario.setUsuario(usarioComenta);
		comentario.setTweet(tweetComentado);
		comentario.setConteudo("Teste de inserção de comentário!");

		this.comentarioRepository.inserir(comentario);

		Comentario comentarioInserido = this.comentarioRepository.consultar(comentario.getId());

		assertThat(comentarioInserido.getId()).isGreaterThan(0);
		assertThat(comentarioInserido).isNotNull();
		assertThat(Calendar.getInstance().getTime()).isCloseTo(comentarioInserido.getData().getTime(), DELTA_MILIS);

		System.out.println(comentario);
	}

	@Test
	public void testa_consultar_comentario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat(comentario).isNotNull();
		assertThat(comentario.getId()).isEqualTo(ID_COMENTARIO_CONSULTA);
		assertThat(comentario.getConteudo()).isNotNull();
		assertThat(comentario.getUsuario()).isNotNull();
		assertThat(comentario.getTweet()).isNotNull();
		assertThat(comentario.getConteudo()).isEqualTo("Comentário 1");

		System.out.println(comentario);
	}

	@Test
	public void testa_alterar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		comentario.setConteudo("Comentario alterado 1");

		this.comentarioRepository.atualizar(comentario);

		Comentario alterado = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat(alterado).isNotNull();
		assertThat(alterado.getId()).isEqualTo(ID_COMENTARIO_CONSULTA);
		assertThat(alterado.getConteudo()).isEqualTo(comentario.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getData().getTime(), DELTA_MILIS);

		System.out.println(comentario);
	}

	@Test
	public void testa_remover_comentario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat(comentario).isNotNull();

		this.comentarioRepository.remover(ID_COMENTARIO_CONSULTA);

		Comentario comentarioRemovido = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		assertThat(comentarioRemovido).isNull();

		System.out.println(comentarioRemovido);
	}

	@Test
	public void testa_listar_todos_os_comentarios() {
		List<Comentario> comentarios = new ArrayList<Comentario>();

		comentarios = this.comentarioRepository.listarTodos();

		assertThat(comentarios).isNotNull().isNotEmpty().hasSize(10).extracting("conteudo").containsExactlyInAnyOrder(
				"Comentário 1", "Comentário 2", "Comentário 3", "Comentário 4", "Comentário 5", "Comentário 6",
				"Comentário 7", "Comentário 8", "Comentário 9", "Comentário 10");

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});

	}

	// Pesquisa todos comentários com o id inserido no seletor
	@Test
	public void testa_pesquisar_comentarios_filtratos_por_id_comentario() {

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setId(ID_COMENTARIO_CONSULTA); // id 1

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getId()).isEqualTo(ID_COMENTARIO_CONSULTA);
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});
	}

	// Pesquisa de comentários somente com parte de conteudo.
	@Test
	public void testa_pesquisar_comentarios_filtrados_por_parte_de_comentario() {

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setConteudo("Comentário");

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});
	}

	// Dada uma data traz a lista de comentários dessa data
	@Test
	public void testa_pesquisar_comentarios_filtratos_por_data()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(3);

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setData(comentario.getData());

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});
	}

	// Dada uma data traz a lista de comentários em um periodo
	public void testa_pesquisar_comentarios_filtratos_por_periodo()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(1);
		Comentario comentario2 = this.comentarioRepository.consultar(5);

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setData(comentario.getData());
		seletor.setData_final(comentario2.getData());

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});
	}

	// Dado um id de tweet, traz uma lista de todos os comentários feito no tweet
	@Test
	public void testa_pesquisar_comentarios_feito_por_um_usuario()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdTweet(1);

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c);
		});
	}

	// Dado um tweet, pesquisar os comentarios de dado usuários - c.idTweet =
	// idTweet && c.idUsuario = idUsuario
	@Test
	public void pesquisar_comentario_feito_por_um_usuário_em_um_tweet()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		seletor.setIdTweet(1);

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c.getConteudo());
		});
	}

	// Dado um tweet, pesquisar os comentarios de dado usuários, em determinada
	// data.
	@Test
	public void pesquisar_comentario_em_um_tweet_por_um_usuario_que_comentou_em_uma_data()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(1);

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		seletor.setIdTweet(1);
		seletor.setData(comentario.getData());

		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);

		comentarios.stream().forEach(c -> {
			assertThat(c.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(c.getUsuario()).isNotNull();
			assertThat(c.getTweet()).isNotNull();
			assertThat(c.getTweet().getUsuario()).isNotNull();
			System.out.println(c.getConteudo());
		});
	}

	// Contar o total de comentários.
	@Test
	public void testa_contar_total_de_comentarios() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		ComentarioSeletor seletor = new ComentarioSeletor();
		long comentarios = this.comentarioRepository.contar(seletor);

		assertThat(comentarios).isEqualTo(10);

		System.out.println(comentarios);
	}

	// Contar o total de comentários feito por um idUsuario
	@Test
	public void testa_contar_total_de_comentarios_por_idUsuario()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(1); // vai dar o usuário de id 4

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(comentario.getUsuario().getId());

		long comentarios = this.comentarioRepository.contar(seletor);

		assertThat(comentarios).isEqualTo(4);

		System.out.println(comentarios);
		System.out.println(comentario);
	}

	// testa contar comentarios por data
	@Test
	public void testa_contar_total_de_comentarios_por_data()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(1); // vai dar o usuário de id 4

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setData(comentario.getData());

		long comentarios = this.comentarioRepository.contar(seletor);

		assertThat(comentarios).isEqualTo(1);

		System.out.println(comentarios);
		System.out.println(comentario);
	}

	// testa contar comentarios por idTweet
	@Test
	public void testa_contar_total_de_comentarios_por_id_tweet()
			throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(1); // vai dar o usuário de id 4

		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdTweet(comentario.getTweet().getId());

		long comentarios = this.comentarioRepository.contar(seletor);

		// assertThat( comentarios ).isEqualTo(1);

		System.out.println(comentarios);
	}

}
