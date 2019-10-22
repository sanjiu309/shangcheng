package chen.mapper;

import chen.entity.ScExample;
import chen.entity.ScKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScMapper {
    int countByExample(ScExample example);

    int deleteByExample(ScExample example);

    int deleteByPrimaryKey(ScKey key);

    int insert(ScKey record);

    int insertSelective(ScKey record);

    List<ScKey> selectByExample(ScExample example);

    int updateByExampleSelective(@Param("record") ScKey record, @Param("example") ScExample example);

    int updateByExample(@Param("record") ScKey record, @Param("example") ScExample example);
}