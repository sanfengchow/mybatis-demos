# mybatis-demos
##你好！
## PR Reviewer Guide 🔍

Here are some key observations to aid the review process:

<table>
<tr><td>⏱️&nbsp;<strong>Estimated effort to review</strong>: 4 🔵🔵🔵🔵⚪</td></tr>
<tr><td>🧪&nbsp;<strong>PR contains tests</strong></td></tr>
<tr><td>🔒&nbsp;<strong>Security concerns</strong><br><br>

否</td></tr>
<tr><td>⚡&nbsp;<strong>Recommended focus areas for review</strong><br><br>

<details><summary><a href='http://tfs2018-web.winning.com.cn:8080/tfs/WN_HIS/人力资源管理/_git/NMS-ALLSTAR/pullrequest/16341?_a=files&path=/NQC/nqc-components/nqc-pdca/nqc-pdca-service/src/main/java/com/winning/nqc/pdca/dotask/service/impl/NqcTaskServiceImpl.java'><strong>条件判断逻辑</strong></a>

在检查结果处理逻辑中，新增了对落实率字段的非空判断。需要验证当检查结果为核查表且结果小于0时，落实率字段是否一定存在有效值，避免空指针异常。
</summary>

```java
if (Double.valueOf(result.getJCJG()) < 0 && StrUtil.isNotEmpty(result.getLSL())) {
    result.setJCJG(decimalFormat.format(Double.valueOf(result.getLSL())));
```

</details>

<details><summary><a href='http://tfs2018-web.winning.com.cn:8080/tfs/WN_HIS/人力资源管理/_git/NMS-ALLSTAR/pullrequest/16341?_a=files&path=/NQC/nqc-components/nqc-pdca/nqc-pdca-service/src/main/java/com/winning/nqc/pdca/rectify/service/impl/XfzgjhPlgjfxServiceImpl.java'><strong>重复数据处理</strong></a>

在责任人、被查护士、被查病人信息处理逻辑中，存在重复的流式操作。需要确认是否确实需要分别处理被查护士和被查病人列表，以及合并逻辑是否正确。
</summary>

```java
var bcrInfoList = kfxList.stream().filter(p -> StrUtil.isNotEmpty(p.getBCHSMC())).map(p -> formatUserName(p.getBCHSDM(), p.getBCHSMC())).collect(Collectors.toList());
var bchsList = kfxList.stream().filter(p -> StrUtil.isNotEmpty(p.getBCHSMC())).map(p -> formatUserName(p.getBCHSDM(), p.getBCHSMC())).collect(Collectors.toList());
var bcbrList = kfxList.stream().filter(p -> StrUtil.isNotEmpty(p.getBCBRMC())).map(p -> formatUserName(p.getBCBRDM(), p.getBCBRMC())).collect(Collectors.toList());
bcrInfoList.addAll(bcbrList);
```

</details>

<details><summary><a href='http://tfs2018-web.winning.com.cn:8080/tfs/WN_HIS/人力资源管理/_git/NMS-ALLSTAR/pullrequest/16341?_a=files&path=/NQC/nqc-components/nqc-pdca/nqc-pdca-service/src/main/java/com/winning/nqc/pdca/common/impl/WordServiceImpl.java'><strong>表格单元格合并</strong></a>

新增了水平单元格合并方法，需要验证在复杂的表格结构中合并逻辑是否正确，特别是当表格行数动态变化时的边界情况处理。
</summary>

```java
public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
    for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
        XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
        if (cellIndex == fromCell) {
            // The first merged cell is set with RESTART merge value
            cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
        } else {
            // Cells which join (merge) the first one, are set with CONTINUE
            cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
        }
    }
}
```

</details>

</td></tr>
</table>
