/*
「プロになるJava」サンプル
https://gihyo.jp/book/2022/978-4-297-12685-8

Takaaki Sugiyama 2022 copyright reserved.
License: CC0 1.0 Universal
*/

package life.hyakusho.share;

import life.hyakusho.share.HomeController.TaskItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ShareDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    ShareDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert =
                new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("tasklist");
        insert.execute(param);
    }
    public List<TaskItem> findAll() {
        String query = "SELECT * FROM tasklist ORDER BY startdatetime";
        List<Map<String,Object>> result = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("place").toString(),
                        row.get("groupname").toString(),
                        row.get("startdatetime").toString(),
                        row.get("enddatetime").toString(),
                        (Boolean)row.get("done")))
                .toList();
        return taskItems;
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
        return number;
    }

    public int update(TaskItem taskItem) {
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET place = ?, groupname = ?, startdatetime = ?, enddatetime = ?, done = ? WHERE id = ?",
                taskItem.place(),
                taskItem.groupname(),
                taskItem.startdatetime(),
                taskItem.enddatetime(),
                taskItem.done(),
                taskItem.id());
        return number;
    }
}