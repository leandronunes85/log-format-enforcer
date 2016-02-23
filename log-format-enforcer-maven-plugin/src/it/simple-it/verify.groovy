File generatedDir = new File(basedir, "target/generated-sources/log-format-enforcer/com/leandronunes85/log_format_enforcer/simple/it");
assert generatedDir.isDirectory()

File generatedFile = new File(generatedDir, "LogFormatEnforcer.java");
assert generatedFile.isFile()
