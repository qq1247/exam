package com.wcpdoc.exam.base.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.base.entity.Org;
import com.wcpdoc.exam.base.entity.User;
import com.wcpdoc.exam.base.entity.UserXlsx;
import com.wcpdoc.exam.base.service.OrgService;
import com.wcpdoc.exam.base.service.UserService;
import com.wcpdoc.exam.base.service.UserXlsxService;
import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.impl.BaseServiceImp;
import com.wcpdoc.exam.core.util.EncryptUtil;

/**
 * 组织机构表格服务层实现
 * 
 * v1.0 zhanghc 2016-5-8上午11:00:00
 */
@Service
public class UserXlsxServiceImpl extends BaseServiceImp<Object> implements UserXlsxService {
	
	@Resource
	private OrgService orgService;
	
	@Resource
	private UserService userService;
	
	@Override
	@Resource(name = "orgDaoImpl")
	public void setDao(BaseDao<Object> dao) {
		super.dao = dao;
	}

	@Override
	public void inputUserXlsx(MultipartFile file) {
		// 解析Excel,读取内容,path Excel路径
		// 文件路径 test.xlsx 文件在代码打包里面，给他这个excel放到C盘根目录下
		String path = "E:\\auser.xlsx";
		InputStream input = null;
		if (path != null && path.length() > 7) {
			// 判断文件是否是Excel(2003、2007)
			String suffix = path.substring(path.lastIndexOf("."), path.length());
			try {
				input = file.getInputStream();
			} catch (Exception e) {
				throw new MyException("未找到指定的文件！");
			}
			// Excel2003
			if (".xls".equals(suffix)) {
				POIFSFileSystem fileSystem = null;
				// 工作簿
				HSSFWorkbook workBook = null;
				try {
					fileSystem = new POIFSFileSystem(input);
					workBook = new HSSFWorkbook(fileSystem);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 获取第一个工作簿
				HSSFSheet sheet = workBook.getSheetAt(0);
				getContent((Sheet) sheet);
				// Excel2007
			} else if (".xlsx".equals(suffix)) {
				XSSFWorkbook workBook = null;
				try {
					workBook = new XSSFWorkbook(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 获取第一个工作簿
				XSSFSheet sheet = workBook.getSheetAt(0);
				getContent((Sheet) sheet);
			}
		} else {
			System.out.println("非法的文件路径!");
		}
	}
	

	//获取Excel内容
	@SuppressWarnings("deprecation")
	public void getContent(Sheet sheet) {
		// Excel数据总行数
		int rowCount = sheet.getPhysicalNumberOfRows();
		//判断模板是否正确
		if(rowCount > 2){
			Row row = sheet.getRow(1);
			int cellCount = row.getPhysicalNumberOfCells();
			for (int j = 0; j < cellCount; j++) {
				Cell cell = row.getCell(j);
				switch (j) {
				case 0:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						if(!"用户昵称".equals(cell.getStringCellValue())){
							throw new MyException("模板有误！");
						}
					}
					break;
				case 1:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						if(!"用户登录名".equals(cell.getStringCellValue())){
							throw new MyException("模板有误！");
						}
					}
					break;
				case 2:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						if(!"用户所属机构".equals(cell.getStringCellValue())){
							throw new MyException("模板有误！");
						}
					}
					break;
				}
			}
		}
		
		// 遍历数据行，略过标题行，从第三行开始
		for (int i = 2; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			int cellCount = row.getPhysicalNumberOfCells();
			// 遍历行单元格
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < cellCount; j++) {
				Cell cell = row.getCell(j);
				switch (j) {
				case 0:
					// 根据cell中的类型来输出数据
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						map.put("name", cell.getStringCellValue());
					}
					break;
				case 1:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						map.put("loginName", cell.getStringCellValue());
					}
					break;
				case 2:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						map.put("orgName", cell.getStringCellValue());
					}
					break;
				}
			}
			addContent(map);
		}
	}
	
	//保存Excel内容
	public void addContent(Map<String, Object> map) {
		//map.get("name");		map.get("loginName");		map.get("orgName");
		Org org = orgService.getOrg(map.get("orgName").toString());
		User user = userService.getUser(map.get("loginName").toString());
		if (user == null) {
			Date date = new Date();
			User entity = new User();
			entity.setName(map.get("name").toString());
			entity.setLoginName(map.get("loginName").toString());
			entity.setPwd(EncryptUtil.md52Base64(map.get("loginName").toString() + "111111"));
			entity.setRegistTime(date);
			entity.setOrgId(org.getId());
			entity.setUpdateTime(date);
			entity.setUpdateUserId(1); //getCurUser().getId()	
			entity.setState(1);
			userService.add(entity);
			return;
		}
		if (user != null && org.getId() != user.getOrgId()) {
			Date date = new Date();
			User entity = new User();
			entity.setName(map.get("name").toString());
			entity.setLoginName(map.get("loginName").toString());
			entity.setPwd(EncryptUtil.md52Base64(map.get("loginName").toString() + "111111"));
			entity.setRegistTime(date);
			entity.setOrgId(org.getId());
			entity.setUpdateTime(date);
			entity.setUpdateUserId(1); //getCurUser().getId()	
			entity.setState(1);
			userService.add(entity);
			return;
		}
		if (user != null && org.getId() == user.getOrgId()) {
			if (map.get("name").toString().equals(user.getName()) ) {
				return;
			}
			user.setName(map.get("name").toString());
			user.setUpdateTime(new Date());
			user.setUpdateUserId(1); //getCurUser().getId()
			userService.update(user);
			return;
		}
	}
	
	@Override
	public void exportUserXlsx(String ids) {
		List<UserXlsx> userXlsx = new ArrayList<UserXlsx>();
		
		String[] split = ids.split(",");
		for(String idString : split){
			User user = userService.getEntity(Integer.parseInt(idString));
			Org org = orgService.getEntity(user.getOrgId().intValue());
			userXlsx.add( new UserXlsx(user.getName(), user.getLoginName(), org.getName()));
		}

		try {
			FileOutputStream os = new FileOutputStream("target/classes/res/auser.xlsx");
			Context context = new Context();
	        //将列表参数放入context中
	        context.putVar("userXlsxList", userXlsx);
			InputStream inputStream = this.getClass().getResourceAsStream("/res/userTemplate.xlsx");
			JxlsHelper jxlsHelper = JxlsHelper.getInstance();
			Transformer transformer = jxlsHelper.createTransformer(inputStream, os);
			JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
			Map<String, Object> funcs = new HashMap<String, Object>();
			evaluator.getJexlEngine().setFunctions(funcs);
			jxlsHelper.processTemplate(context, transformer);
			os.close();

           //浏览器下载
           //指定数据生成后的文件输入流（将上述out的路径作为文件的输入流）
           @SuppressWarnings("resource")
           FileInputStream fileInputStream = new FileInputStream("target/classes/res/auser.xlsx");
           //导出excel文件，设置文件名
           String filename = URLEncoder.encode("用户信息表.xlsx", "UTF-8");
           //设置下载头
           response.setHeader("Content-Disposition", "attachment;filename=" + filename);
           ServletOutputStream outputStream = response.getOutputStream();
           //将文件写入浏览器
           byte[] bys = new byte[fileInputStream.available()];
           fileInputStream.read(bys);
           outputStream.write(bys);
           outputStream.flush();
           outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void templateUserXlsx() {
		try {
           //浏览器下载
           //指定数据生成后的文件输入流（将上述out的路径作为文件的输入流）
           @SuppressWarnings("resource")
           FileInputStream fileInputStream = new FileInputStream("target/classes/res/userExample.xlsx");
           //导出excel文件，设置文件名
           String filename = URLEncoder.encode("用户信息表模板.xlsx", "UTF-8");
           //设置下载头
           response.setHeader("Content-Disposition", "attachment;filename=" + filename);
           ServletOutputStream outputStream = response.getOutputStream();
           //将文件写入浏览器
           byte[] bys = new byte[fileInputStream.available()];
           fileInputStream.read(bys);
           outputStream.write(bys);
           outputStream.flush();
           outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
