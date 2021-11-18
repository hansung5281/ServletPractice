package com.newlecture.web.controller.admin.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] openIds = request.getParameterValues("open-id");
		String[] delIds = request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		String ids_ = request.getParameter("ids");
		String[] ids = ids_.trim().split(" ");

		NoticeService service = new NoticeService();
		if(openIds != null) {
			switch (cmd) {
			case "�ϰ�����":
				List<String> oids = Arrays.asList(openIds);
				List<String> cids = new ArrayList(Arrays.asList(ids));
				cids.removeAll(oids);
				
				for (int i = 0; i < ids.length; i++) {
					// 1.���� id�� open �� ���³�
					if (oids.contains(ids[i])) {
						
					}else {
						
					}
				}
				
				service.pubNoticeAll(oids,cids); 
				break;
			case "�ϰ�����":
				int[] ids1 = new int[delIds.length];
				for (int i = 0; i < delIds.length; i++) {
					ids1[i] = Integer.parseInt(delIds[i]);
				}
				int results = service.deleteNoticeAll(ids1);
				break;
			}
		}
		response.sendRedirect("list");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String filed_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		String page_ = request.getParameter("p");

		String filed = "title";
		if (filed_ != null && !filed_.equals("")) {
			filed = filed_;
		}
		String query = "";
		if (query_ != null && !query_.equals("")) {
			query = query_;
		}

		int page = 1;
		if (page_ != null && !page_.equals("")) {
			page = Integer.parseInt(page_);
		}

		NoticeService service = new NoticeService();
		List<NoticeView> list = service.getNoticeList(filed, query, page);
		int count = service.getNoticeCount(filed, query);

		request.setAttribute("list", list);
		request.setAttribute("count", count);

		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp").forward(request, response);
	}
}
