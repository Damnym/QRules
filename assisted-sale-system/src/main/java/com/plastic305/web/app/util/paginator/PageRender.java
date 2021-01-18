package com.plastic305.web.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T> page;
	private int pagesTotal;
	private int elementByPage;
	private int actualPage;

	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		elementByPage = page.getSize();
		pagesTotal = page.getTotalPages();
		actualPage = page.getNumber() + 1;

		int from, to;
		if (pagesTotal <= elementByPage) {
			from = 1;
			to = pagesTotal;
		} else if (actualPage <= elementByPage / 2) {
			from = 1;
			to = elementByPage;
		} else if (actualPage >= pagesTotal - elementByPage / 2) {
			from = pagesTotal - elementByPage + 1;
			to = elementByPage;
		} else {
			from = actualPage - elementByPage / 2;
			to = elementByPage;
		}

		for (int i = 0; i < to; i++) {
			pages.add(new PageItem(from + i, actualPage == from + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getPagesTotal() {
		return pagesTotal;
	}

	public int getActualPage() {
		return actualPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	public boolean isFirst() {
		return this.page.isFirst();
	}

	public boolean isLast() {
		return this.page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}