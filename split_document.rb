docs_path = File.join(File.dirname(__FILE__), "/target/classes/cran/", 'cran.all.1400')
docs = File.read(docs_path)
docs.gsub!("\n", " ").gsub!("  ", " ")
docs = docs.split(".I")
i = 0
docs.each do |doc|
    m = ((/.W.*/).match doc)
    t = ((/.T.*.A/).match doc)
    m = m[0] if m
    t = t[0] if t
    if !m.nil?
        m = m.gsub(".W ","").gsub("-"," ")
    end
    if !t.nil?
        t = t.gsub(".A","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "").gsub(".T ", "")
    end
    new_path = File.join(File.dirname(__FILE__), "/target/classes/cran/", "parsed/docs/")
    out_file = File.new(new_path + i.to_s, "w")
    out_file.puts(m.to_s + t.to_s*2)
    out_file.close
    i = i+1
end

docs_path = File.join(File.dirname(__FILE__), "/target/classes/cran/", 'cran.qry')
docs = File.read(docs_path)
docs.gsub!("\r\n", " ").gsub!("  ", " ")
docs = docs.split(".I")
i = 0
docs.each do |doc|
    m = ((/.W.*/).match doc)
    m = m[0] if m
    if !m.nil?
        m = m.gsub(".W ","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "")
    end
    new_path = File.join(File.dirname(__FILE__), "/target/classes/cran/", "parsed/query/")
    out_file = File.new(new_path + i.to_s, "w")
    out_file.puts(m)
    out_file.close
    i = i+1
end