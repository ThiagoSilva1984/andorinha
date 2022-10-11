package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository {

	public void inserir(Comentario comentario) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			int id = this.recuperaProximoValorDaSequence("seq_comentario");
			comentario.setId(id); 
			
			Calendar hoje = Calendar.getInstance();

			PreparedStatement ps = c.prepareStatement(
					"insert into comentario (id, id_usuario, id_tweet, conteudo, data_postagem) values (?,?,?,?,?)");
			ps.setInt(1, comentario.getId());
			ps.setInt(2, comentario.getUsuario().getId());
			ps.setInt(3, comentario.getTweet().getId());
			ps.setString(4, comentario.getConteudo());
			ps.setTimestamp(5, new Timestamp(hoje.getTimeInMillis()));

			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir comentario", e);
		}
	}

	public void atualizar(Comentario comentario) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			Calendar hoje = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("UPDATE comentario SET conteudo = ?, data_postagem = ?  WHERE id = ?");
			ps.setString(1, comentario.getConteudo());
			ps.setTimestamp(2, new Timestamp(hoje.getTimeInMillis()));
			ps.setInt(3, comentario.getId());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar comentário", e);
		}
	}

	public void remover(int id) throws ErroAoConsultarBaseException, ErroAoconectarNaBaseException {
		try (Connection c = super.ds.getConnection()) {
			PreparedStatement ps = c.prepareStatement("DELETE FROM comentario WHERE id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao deletar comentário", e);
		}
	}

	public Comentario consultar(int id) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			Comentario comentario = null;

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.id, c.conteudo, c.data_postagem, c.id_usuario, c.id_tweet, ");
			sql.append("u.nome as nome_usuario, t.conteudo as conteudo_tweet, t.data_postagem as data_postagem_tweet, ");
			sql.append("t.id_usuario as id_usuario_tweet, ut.nome as nome_usuario_tweet ");
			sql.append("FROM comentario c ");
			sql.append("JOIN tweet t on c.id_tweet = t.id ");
			sql.append("JOIN usuario u on c.id_usuario = u.id ");
			sql.append("JOIN usuario ut on t.id_usuario = ut.id ");
			sql.append("WHERE c.id = ? ");

			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) { // se veio o resultado
				Usuario usuarioTweet = new Usuario();
				usuarioTweet.setId(rs.getInt("id_usuario_tweet"));
				usuarioTweet.setNome(rs.getString("nome_usuario_tweet"));

				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setConteudo(rs.getString("conteudo_tweet"));
				Calendar dataTweet = new GregorianCalendar();
				dataTweet.setTime(rs.getTimestamp("data_postagem_tweet"));
				tweet.setData_postagem(dataTweet);
				tweet.setUsuario(usuarioTweet);

				comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setTweet(tweet);
				comentario.setUsuario(usuarioTweet);
				comentario.setConteudo(rs.getString("conteudo"));

				Calendar dataComentario = new GregorianCalendar();
				dataComentario.setTime(rs.getTimestamp("data_postagem"));
				comentario.setData(dataComentario);
			}
			rs.close();
			ps.close();

			return comentario;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar comentario", e);
		}
	}
	
//	public List<Comentario> pesquisar(ComentarioSeletor seletor) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
//		
//		//listar os comentarios, filtrando pelos campos do seletor
//
//		return null;
//	}
//
//	public Long contar(ComentarioSeletor seletor) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
//	
//		//listar os comentarios, filtrando pelos campos do seletor
//
//		return 0L;
//	}

	public List<Comentario> listarTodos() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			List<Comentario> comentarios = new ArrayList<Comentario>();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.id, c.conteudo, c.data_postagem, c.id_usuario, c.id_tweet, ");
			sql.append("u.nome as nome_usuario, t.conteudo as conteudo_tweet, t.data_postagem as data_postagem_tweet, ");
			sql.append("t.id_usuario as id_usuario_tweet, ut.nome as nome_usuario_tweet ");
			sql.append("FROM comentario c ");
			sql.append("JOIN tweet t on c.id_tweet = t.id ");
			sql.append("JOIN usuario u on c.id_usuario = u.id ");
			sql.append("JOIN usuario ut on t.id_usuario = ut.id ");

			PreparedStatement ps = c.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Usuario usuarioTweet = new Usuario();
				usuarioTweet.setId(rs.getInt("id_usuario_tweet"));
				usuarioTweet.setNome(rs.getString("nome_usuario_tweet"));

				Calendar dataTweet = new GregorianCalendar();
				dataTweet.setTime(rs.getTimestamp("data_postagem_tweet"));

				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setConteudo(rs.getString("conteudo_tweet"));
				tweet.setData_postagem(dataTweet);
				tweet.setUsuario(usuarioTweet);

				Calendar dataComnetario = new GregorianCalendar();
				dataComnetario.setTime(rs.getTimestamp("data_postagem"));

				Comentario comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setData(dataComnetario);
				comentario.setConteudo(rs.getString("conteudo"));
				comentario.setUsuario(usuarioTweet);
				comentario.setTweet(tweet);

				comentarios.add(comentario);
			}
			rs.close();
			ps.close();

			return comentarios;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar comentários", e);
		}
	}

}
