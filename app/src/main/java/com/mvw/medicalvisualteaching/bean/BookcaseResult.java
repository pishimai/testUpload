package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;
import java.util.List;

public class BookcaseResult implements Serializable{
	private List<Book> books;
	private List<BookcaseRotateData> rotateData;

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<BookcaseRotateData> getRotateData() {
		return rotateData;
	}

	public void setRotateData(
			List<BookcaseRotateData> rotateData) {
		this.rotateData = rotateData;
	}
}
