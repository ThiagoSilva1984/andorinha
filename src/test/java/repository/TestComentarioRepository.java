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
		comentario.setTweet( tweetComentado );
		
		comentario.setConteudo("Comentando o tweet 1");
		
		this.comentarioRepository.inserir(comentario);
		
		Comentario comentarioInserido = this.comentarioRepository.consultar(comentario.getId());
	
		assertThat( comentarioInserido.getId() ).isGreaterThan(0);	
		assertThat( comentarioInserido ).isNotNull();
		assertThat( Calendar.getInstance().getTime() ).isCloseTo(comentarioInserido.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_consultar_comentario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat( comentario ).isNotNull();
		assertThat( comentario.getId() ).isEqualTo(ID_COMENTARIO_CONSULTA);
		assertThat( comentario.getConteudo() ).isNotNull();
		assertThat( comentario.getUsuario() ).isNotNull();
		assertThat( comentario.getTweet() ).isNotNull();
		assertThat( comentario.getConteudo() ).isEqualTo("Comentário 1");
	}
	
	@Test
	public void testa_alterar_usuario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		comentario.setConteudo("Comentario atualizado 1");
		
		this.comentarioRepository.atualizar(comentario);	
		
		Comentario alterado = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);	

		assertThat( alterado ).isNotNull();
		assertThat( alterado.getId() ).isEqualTo(ID_USUARIO_CONSULTA);
		assertThat( alterado.getConteudo() ).isEqualTo(comentario.getConteudo());
		assertThat( Calendar.getInstance().getTime() ).isCloseTo(alterado.getData().getTime(), DELTA_MILIS);
	}
	
	@Test
	public void testa_remover_comentario() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		assertThat( comentario ).isNotNull();
		
		this.comentarioRepository.remover(ID_COMENTARIO_CONSULTA);
		
		Comentario comentarioRemovido = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);	
		assertThat( comentarioRemovido ).isNull();			
	}
	
	@Test
	public void testa_listar_usuarios() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		List<Comentario> comentarios = new ArrayList<Comentario>();

		comentarios = this.comentarioRepository.listarTodos();

		assertThat(comentarios).isNotNull().isNotEmpty().hasSize(10).extracting("conteudo").containsExactlyInAnyOrder(
				"Comentário 1", "Comentário 2", "Comentário 3", "Comentário 4", "Comentário 5", "Comentário 6",
				"Comentário 7", "Comentário 8", "Comentário 9", "Comentário 10");

		comentarios.stream().forEach(t -> {
			assertThat(t.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			assertThat(t.getTweet()).isNotNull();
			assertThat(t.getTweet().getUsuario()).isNotNull();
		});
	}
	
}
