
package ml.kalanblowSystemManagement.utils.paging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Pager {
    private int buttonsToShow = 5;

    private int startPage;

    private int endPage;

    private List<Integer> pageSizesToShow = new ArrayList<>();

    public Pager(int totalPages, int currentPage, int buttonsToShow, long totalSize) {

        setButtonsToShow(buttonsToShow);
        setPageSizesToShow(totalSize);

        int halfPagesToShow = getButtonsTow() / 2;

        if (totalPages <= getButtonsTow()) {
            setStartPage(1);
            setEndPage(totalPages);
        }
        else if (currentPage - halfPagesToShow <= 0) {
            setStartPage(1);
            setEndPage(getButtonsTow());
        }
        else if (currentPage + halfPagesToShow == totalPages) {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(totalPages);
        }
        else if (currentPage + halfPagesToShow > totalPages) {
            setStartPage(totalPages - getButtonsTow() + 1);
            setEndPage(totalPages);
        }
        else {

            setStartPage(currentPage - halfPagesToShow);
            setEndPage(currentPage + halfPagesToShow);
        }

    }

    public int getStartPage() {

        return startPage;
    }

    private void setEndPage(int totalPages) {

    }

    private void setStartPage(int i) {
        this.startPage = i;
    }

    private int getButtonsTow() {

        return buttonsToShow;
    }

    public int getButtonsToShow() {
        return buttonsToShow;
    }

    public void setButtonsToShow(int buttonsToShow) {

        if (buttonsToShow % 2 != 0) {

            this.buttonsToShow = buttonsToShow;
        }
        else {
            throw new IllegalArgumentException("Must be an odd value!");
        }
    }

    public int getEndPage() {
        return endPage;
    }

    public List<Integer> getPageSizesToShow() {
        return pageSizesToShow;
    }

    public String getPageSizesToShowInJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(pageSizesToShow);
    }

    public void setPageSizesToShow(List<Integer> pageSizesToShow) {
        this.pageSizesToShow = pageSizesToShow;
    }

    public void setPageSizesToShow(long totalSize) {
        this.pageSizesToShow = Arrays.stream(InitialPagingSizes.PAGE_SIZES).boxed()
                .filter(pageSize -> totalSize > pageSize).collect(Collectors.toList());
    }

}
