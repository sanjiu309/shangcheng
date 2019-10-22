package chen.e3.content.service;

import java.util.List;

import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;
import chen.e3.utils.TreeNode;


public interface ContentCategoryService {

	List<TreeNode> getContentCategoryList(Long parentId);
	E3Result addContentCate(Long parentId,String name);
	void updateContentCategory(Long id, String name);
	E3Result deleteContentCategory(Long id);
}
