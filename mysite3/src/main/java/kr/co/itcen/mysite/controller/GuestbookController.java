package kr.co.itcen.mysite.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itcen.mysite.repository.GuestbookDao;
import kr.co.itcen.mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	private static final Log LOG = LogFactory.getLog(GuestbookController.class);

	@Autowired // DI
	private GuestbookDao guestbookDao;

	@RequestMapping(value = "/list")
	public String index(Model model) {
		List<GuestbookVo> list = guestbookDao.getList();
		model.addAttribute("list", list);
		return "guestbook/index";
	}
	
	// 방명록(ajax) 리스트 접근
	@RequestMapping(value = "/ajax-list")
	public String indexAjax(Model model) {
		List<GuestbookVo> list = guestbookDao.getList();
		model.addAttribute("list", list);
		return "guestbook/index-ajax";
	}
	
	// 방명록(ajax) 리스트 접근
		@RequestMapping(value = "/ajax-guestbook-list")
		public String indexGuestbookAjax(Model model) {
			List<GuestbookVo> list = guestbookDao.getList();
			model.addAttribute("list", list);
			return "guestbook/index-guestbook-ajax";
		}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam("no") Long no, Model model) {
		model.addAttribute("no", no);
		return "guestbook/delete";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@ModelAttribute GuestbookVo vo) {
		guestbookDao.delete(vo);
		return "redirect:/guestbook/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(
			@ModelAttribute GuestbookVo vo) {
		guestbookDao.insert(vo);
		return "redirect:/guestbook/list";
	}
	
	// 방명록(ajax) 데이터 추가
	@RequestMapping(value = "/addAjax", method = RequestMethod.POST)
	public String addAjaxList(@ModelAttribute GuestbookVo vo) {
		guestbookDao.insert(vo);
		return "redirect:/guestbook/ajax-list";
	}
}
