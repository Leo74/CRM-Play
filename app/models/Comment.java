package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.*;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import play.data.validation.ValidationError;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@Entity
public class Comment extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	@Column(unique=true)
	public String title;

	@Required
	@Column(unique=true)
	public String content;

	public static Finder<Long,Comment> find = new Finder<Long,Comment>(Long.class, Comment.class);

	public static List<Comment> findAll() {
		return find.all();
	}

}