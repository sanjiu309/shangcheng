package chen.e3.service;

import java.util.List;

import chen.e3.utils.TreeNode;

/**
 * 商品类别
 * @author 陈
 *
 */
public interface ItemCatService {

	List<TreeNode> getItemCatList(Long parentId);
}
