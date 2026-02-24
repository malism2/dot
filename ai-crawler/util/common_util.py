import logging
import re
import os
from urllib.parse import urlparse
from PIL import Image

# 设置日志记录
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(filename)s - %(funcName)s - %(lineno)d - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class CommonUtil:
    def detail_handle(self,detail):
        if detail:
            index1 = detail.find("#")
            index2 = detail.find("*")

            if index1 != -1 and index2 != -1:
                index = min(index1, index2)
                substring = detail[index:]
                return re.sub(r'\*\*(.+?)\*\*', '### \\1', substring)
            elif index1 != -1:
                substring = detail[index1:]
                return re.sub(r'\*\*(.+?)\*\*', '### \\1', substring)
            elif index2 != -1:
                substring = detail[index2:]
                return re.sub(r'\*\*(.+?)\*\*', '### \\1', substring)
            else:
                return re.sub(r'\*\*(.+?)\*\*', '### \\1', detail)
        else:
            return None

    # 根据url提取域名/path，返回为-拼接的方式
    @staticmethod
    def get_name_by_url(url):
        if url:
            domain = urlparse(url).netloc
            path= urlparse(url).path
            if path and path.endswith("/"):
                path = path[:-1]
            return (domain.replace("www.","") + path.replace("/", "-")).replace(".", "-")
        else:
            return None

    @staticmethod
    def png_to_webp_tiny(png_path, output_path=None, quality=75, lossless=False, method=6):
        if not os.path.exists(png_path) or not png_path.lower().endswith('.png'):
            raise ValueError("输入文件不是有效 PNG 图片")
        if output_path is None:
            output_path = os.path.splitext(png_path)[0] + '.webp'

        try:
            # 打开 PNG 图片（保留透明通道）
            with Image.open(png_path) as img:
                # 关键参数：
                # - quality: 有损压缩质量，80-90 是「画质+体积」黄金区间
                # - lossless: 无损压缩（True 时 quality 无效）
                # - method: 压缩算法复杂度，6 为极致压缩（推荐）
                # - optimize: 开启优化（默认 True）
                # - alpha_quality: 透明通道质量（1-100，默认 100，可降为 80 进一步压缩）
                img.save(
                    output_path,
                    format='WebP',
                    quality=quality,
                    lossless=lossless,
                    method=method,
                    alpha_quality=80,  # 透明通道轻量化（不影响视觉效果）
                    optimize=True
                )
            
            # 计算压缩率
            png_size = os.path.getsize(png_path) / 1024  # KB
            webp_size = os.path.getsize(output_path) / 1024  # KB
            compression_rate = (1 - webp_size / png_size) * 100
            
            print(f"✅ 转换成功！")
            print(f"原 PNG 大小: {png_size:.2f} KB")
            print(f"WebP 大小: {webp_size:.2f} KB")
            print(f"压缩率: {compression_rate:.2f}%")
            return output_path
    
        except Exception as e:
            print(f"❌ 转换失败：{str(e)}")
            return None                

