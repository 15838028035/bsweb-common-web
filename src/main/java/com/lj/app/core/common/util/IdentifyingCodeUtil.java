package com.lj.app.core.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lj.app.core.common.web.Struts2Utils;


public class IdentifyingCodeUtil {

	private static Log log = LogFactory.getLog(IdentifyingCodeUtil.class);
    // 验证码图片的宽度。
    private int            width     = 120;
    // 验证码图片的高度。
    private int            height    = 40;
    // 验证码字符个数
    private int            codeCount = 4;

    // 验证码干扰线数
    private int            lineCount = 10;
	
	/**
	 * 生成图片校验码
	 */
	public  String getIdentifyingCode(){
		// 在内存中创建图象 
	 	int width = 120, height = 40;
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
	
	 public String getIdentifyingCodeNew() {

	        Graphics2D g = null;
	        try {
	        	String type = Struts2Utils.getParameter("type");//验证码type
	        	
	            int fontWidth = width / codeCount;// 字体的宽度
	            int fontHeight = height - 5;// 字体的高度
	            int codeY = height - 8;

	            // 图像buffer
	            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            g = buffImg.createGraphics();
	            g.setColor(new Color(255, 255, 255));
	            g.fillRect(0, 0, width, height);

	            g.setColor(new Color(204, 204, 204));
	            g.drawRect(0, 0, width - 1, height - 1);

	            // 设置字体
	            // Font font = getFont(fontHeight);
	            Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
	            g.setFont(font);
	            Random random = new Random();
	            lineCount=1;
	            // 设置干扰线
	            for (int i = 0; i < lineCount; i++) {
	                int xs = random.nextInt(width);
	                int ys = random.nextInt(height);
	                int xe = xs + random.nextInt(width);
	                int ye = ys + random.nextInt(height);
	                g.setColor(getRandColor(1, 255));
	                g.drawLine(xs, ys, xe, ye);
	            }

	            // 添加噪点
	            float yawpRate = 0.01f;// 噪声率
	            int area = (int) (yawpRate * width * height);
	                area=8;
	            for (int i = 0; i < area; i++) {
	                int x = random.nextInt(width);
	                int y = random.nextInt(height);

	                buffImg.setRGB(x, y, random.nextInt(255));
	            }

	            String str1 = randomStr(codeCount);// 得到随机字符

	            for (int i = 0; i < codeCount; i++) {
	                String strRand = str1.substring(i, i + 1);
	                g.setColor(getRandColor(1, 255));
	                // g.drawString(a,x,y);
	                // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处

	                g.drawString(strRand, i * fontWidth + 1, codeY);
	            }

	            
	         // 将认证码存入SESSION 
	    	 	if(null == type || type.trim().equals("")){
	    	 		Struts2Utils.getSession().setAttribute("rand", str1);
	    	 	}else{
	    	 		Struts2Utils.getSession().setAttribute(type,str1);
	    	 	}
	    	 	
	            ImageIO.write(buffImg, "JPEG", Struts2Utils.getResponse().getOutputStream());
	        } catch (Exception e) {
	        	log.error("验证码生产失败！", e);
	        } finally {
	            g.dispose();
	        }

	        return null;

	    }

	    // 得到随机字符
	    private String randomStr(int n) {
	        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	        String str2 = "";
	        int len = str1.length() - 1;
	        double r;
	        for (int i = 0; i < n; i++) {
	            r = (Math.random()) * len;
	            str2 = str2 + str1.charAt((int) r);
	        }
	        return str2;
	    }

	    // 得到随机颜色
	    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
	        Random random = new Random();
	        if (fc > 255) fc = 255;
	        if (bc > 255) bc = 255;
	        int r = fc + random.nextInt(bc - fc);
	        int g = fc + random.nextInt(bc - fc);
	        int b = fc + random.nextInt(bc - fc);
	        return new Color(r, g, b);
	    }

	    /**
	     * 产生随机字体
	     */
	    private Font getFont(int size) {
	        Random random = new Random();
	        Font font[] = new Font[5];
	        font[0] = new Font("Ravie", Font.PLAIN, size);
	        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
	        font[2] = new Font("Fixedsys", Font.PLAIN, size);
	        font[3] = new Font("Wide Latin", Font.PLAIN, size);
	        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
	        return font[random.nextInt(5)];
	    }

	    // 扭曲方法
	    private void shear(Graphics g, int w1, int h1, Color color) {
	        shearX(g, w1, h1, color);
	        shearY(g, w1, h1, color);
	    }

	    private void shearX(Graphics g, int w1, int h1, Color color) {
	        Random random = new Random();

	        int period = random.nextInt(2);

	        boolean borderGap = true;
	        int frames = 1;
	        int phase = random.nextInt(2);

	        for (int i = 0; i < h1; i++) {
	            double d = (double) (period >> 1)
	                       * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase)
	                                  / (double) frames);
	            g.copyArea(0, i, w1, 1, (int) d, 0);
	            if (borderGap) {
	                g.setColor(color);
	                g.drawLine((int) d, i, 0, i);
	                g.drawLine((int) d + w1, i, w1, i);
	            }
	        }

	    }

	    private void shearY(Graphics g, int w1, int h1, Color color) {
	        Random random = new Random();
	        int period = random.nextInt(40) + 10; // 50;

	        boolean borderGap = true;
	        int frames = 20;
	        int phase = 7;
	        for (int i = 0; i < w1; i++) {
	            double d = (double) (period >> 1)
	                       * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase)
	                                  / (double) frames);
	            g.copyArea(i, 0, 1, h1, 0, (int) d);
	            if (borderGap) {
	                g.setColor(color);
	                g.drawLine(i, (int) d, i, 0);
	                g.drawLine(i, (int) d + h1, i, h1);
	            }
	        }
	    }
}
