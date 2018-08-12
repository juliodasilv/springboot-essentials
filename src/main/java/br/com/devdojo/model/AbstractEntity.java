package br.com.devdojo.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity implements Serializable{
	private static final long serialVersionUID = 100010072122065656L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		return id != null ? id.equals(other.id) : other.id == null;
	}
}
