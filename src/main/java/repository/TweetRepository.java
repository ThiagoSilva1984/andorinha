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

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;

@Stateless
public class TweetRepository extends AbstractCrudRepository{

	public void inserir(Tweet tweet) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {	
			int id = this.recuperaProximoValorDaSequence("seq_tweet");
			tweet.setId(id);
			
			Calendar hoje = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("insert into tweet (id, id_usuario, conteudo, data_postagem) values (?, ?, ?, ?)");
			ps.setInt(1, tweet.getId());
			ps.setInt(2, tweet.getUsuario().getId());
			ps.setString(3, tweet.getConteudo());
			ps.setTimestamp(4, new Timestamp(hoje.getTimeInMillis()));	
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuario", e);
		}
	}

	public void atualizar(Tweet tweet) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		
		try (Connection c = super.ds.getConnection()) {
			
			Calendar hoje = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("UPDATE tweet SET conteudo = ?, data_postagem = ?  WHERE id = ?");
			ps.setString(1, tweet.getConteudo());
			ps.setTimestamp(2, new Timestamp(hoje.getTimeInMillis()));
			ps.setInt(3, tweet.getId());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuario", e);
		}
	}

	public void remover(int id) throws ErroAoConsultarBaseException, ErroAoconectarNaBaseException {
		try (Connection c = super.ds.getConnection()) {
			PreparedStatement ps = c.prepareStatement("DELETE FROM tweet WHERE id = ?");
			ps.setInt(1, id);		
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao deletar usuario", e);
		}
	}

	public Tweet consultar(int id) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			Tweet tweet = null;
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome as nome FROM tweet t ");
			sql.append("JOIN usuario u on t.id_usuario = u.id ");
			sql.append("WHERE t.id = ? ");

			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				tweet = new Tweet();
				tweet.setId(rs.getInt("id"));
				tweet.setConteudo(rs.getString("conteudo"));
				
				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				tweet.setData_postagem(data);
				
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id_usuario"));
				usuario.setNome(rs.getString("nome"));
				tweet.setUsuario(usuario);
			}
			rs.close();
			ps.close();
			
			return tweet;
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usuario", e);
		}
	}

	public List<Tweet> listarTodos() throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			List<Tweet> tweets = new ArrayList<Tweet>();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.id, t.conteudo, t.id_usuario, t.data_postagem, u.nome FROM tweet t ");
			sql.append("JOIN usuario u on t.id_usuario = u.id ");
			
			PreparedStatement ps = c.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id"));
				tweet.setConteudo(rs.getString("conteudo"));
				
				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				tweet.setData_postagem(data);
				
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id_usuario"));
				usuario.setNome(rs.getString("nome"));
				tweet.setUsuario(usuario);
				
				tweets.add(tweet);			
			}
			rs.close();
			ps.close();

			return tweets;

		} catch (

		SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar tweets", e);
		}
	}

}
