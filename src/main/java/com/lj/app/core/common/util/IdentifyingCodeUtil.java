package com.lj.app.core.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lj.app.core.common.web.Struts2Utils;


public class IdentifyingCodeUtil {

	private static Log log = LogFactory.getLog(IdentifyingCodeUtil.class);
	
	/**
	 * 生成图片校验码
	 */
	public  String getIdentifyingCode(){
		// 在内存中创建图象 
	 	int width = 60, height = 20;
	 	String type = Struts2Utils.getParameter("type");//验证码type
	 	BufferedImage image = new BufferedImage(width, height,
	 			BufferedImage.TYPE_INT_RGB);

	 	// 获取图形上下文 
	 	Graphics g = image.getGraphics();

	 	// 设定背景色 
	 	g.setColor(new Color(0xDCDCDC));
	 	g.fillRect(0, 0, width, height);

	 	//画边框 
	 	g.setColor(Color.black);
	 	g.drawRect(0, 0, width - 1, height - 1);

	 	// 取随机产生的认证码(4位数字) 

	 	String rand = "";
	 	PasswordCoder passwordCode = new PasswordCoder();
	 	passwordCode.setHasLowercase(false);
	 	passwordCode.setHasUppercase(false);
	 	passwordCode.setHasNumber(true);
	 	passwordCode.setHasSpecial(false);
	 	passwordCode.setMaxLen(4);
	 	passwordCode.setMinLen(4);

	 	rand = (passwordCode.generateCode());
	 	
	 	switch (rand.length()) {
	 	case 1:
	 		rand = "000" + rand;
	 		break;
	 	case 2:
	 		rand = "00" + rand;
	 		break;
	 	case 3:
	 		rand = "0" + rand;
	 		break;
	 	default:
	 		rand = rand.substring(0, 4);
	 		break;
	 	}

	 	// 将认证码存入SESSION 
	 	if(null == type || type.trim().equals("")){
	 		Struts2Utils.getSession().setAttribute("rand", rand);
	 	}else{
	 		Struts2Utils.getSession().setAttribute(type,rand);
	 	}
	 	if(log.isDebugEnabled())
	 	    log.debug("Identifying Code=" + rand);
	 	// 将认证码显示到图象中 
	 	g.setColor(Color.black);

	 	g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
	 	String Str = rand.substring(0, 1);
	 	g.drawString(Str, 8, 17);

	 	Str = rand.substring(1, 2);
	 	g.drawString(Str, 20, 15);
	 	Str = rand.substring(2, 3);
	 	g.drawString(Str, 35, 18);

	 	Str = rand.substring(3, 4);
	 	g.drawString(Str, 45, 15);

	 	// 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到 
	 	Random random = new Random(); 
	 	for (int i = 0; i < 20; i++) {
	 		int x = random.nextInt(width);
	 		int y = random.nextInt(height);
	 		g.drawOval(x, y, 0, 0);
	 	}

	 	// 图象生效 
	 	g.dispose();

	 	// 输出图象到页面 
	 	try {
			ImageIO.write(image, "JPEG", Struts2Utils.getResponse().getOutputStream());
		} catch (IOException e) {
			log.error(e.getMessage());
			//e.printStackTrace();
		}
		
		return null ;
	}
}
