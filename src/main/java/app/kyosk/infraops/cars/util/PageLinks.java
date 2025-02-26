package app.kyosk.infraops.cars.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter
public class PageLinks {
    private String first;
    private String previous;
    private String self;
    private String next;
    private String last;

    private static final String _firstPageLink = "%s?page=0&size=%d";
    private static final String _nextPageLink = "%s?page=%d&size=%d";

    public static PageLinks from(Page<?> page, String baseUrl) {
        PageLinks links = new PageLinks();
        baseUrl = baseUrl.contains("?") ? baseUrl.substring(0, baseUrl.indexOf('?')) : baseUrl;
        baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        links.setFirst(String.format(_firstPageLink, baseUrl, page.getSize()));
        links.setSelf(String.format(_nextPageLink, baseUrl, page.getNumber(), page.getSize()));
        if (page.hasPrevious()) {
            links.setPrevious(String.format(_nextPageLink, baseUrl, page.getNumber() - 1, page.getSize()));
        } else {
            links.setPrevious(null);
        }
        if (page.hasNext()) {
            links.setNext(String.format(_nextPageLink, baseUrl, page.getNumber() + 1, page.getSize()));
        } else {
            links.setNext(null);
        }
        links.setLast(String.format(_nextPageLink, baseUrl, page.getTotalPages() - 1, page.getSize()));
        return links;
    }

}
