docs_path = File.join(File.dirname(__FILE__), 'cran.all.1400')
docs = File.read(docs_path)
docs.gsub!("\n", " ").gsub!("  ", " ")
docs = docs.split(".I")
i = 0
# p docs
docs.each do |doc|
    m = ((/.W.*/).match doc)
    t = ((/.T.*.A/).match doc)
    m = m[0] if m
    # p t
    t = t[0] if t
    if !m.nil?
        # m = m.gsub(".W ","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "")
        m = m.gsub(".W ","").gsub("-"," ")
    end
    if !t.nil?
        t = t.gsub(".A","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "").gsub(".T ", "")
    end
    new_path = File.join(File.dirname(__FILE__), "parsed/docs/")
    out_file = File.new(new_path + i.to_s, "w")
    # out_file.puts(m.to_s+t.to_s+t.to_s+t.to_s+t.to_s+t.to_s)
    out_file.puts(m.to_s)
    out_file.close

    # title_path = File.join(File.dirname(__FILE__), "parsed/title/")
    # out_file = File.new(title_path + i.to_s, "w")
    # out_file.puts(t)
    # out_file.close
    i = i+1
end

# docs_path = File.join(File.dirname(__FILE__), 'cran.qry')
# docs = File.read(docs_path)
# docs.gsub!("\r\n", " ").gsub!("  ", " ")
# docs = docs.split(".I")
# i = 0
# docs.each do |doc|
#     m = ((/.W.*/).match doc)
#     m = m[0] if m
#     if !m.nil?
#         m = m.gsub(".W ","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "")
#     end
#     new_path = File.join(File.dirname(__FILE__), "parsed/query/")
#     out_file = File.new(new_path + i.to_s, "w")
#     out_file.puts(m)
#     out_file.close
#     i = i+1
# end

# docs_path = File.join(File.dirname(__FILE__), 'cranqrel')
# File.open(docs_path).each do |line|
#   line = line.split(" ")
#   line.insert(1, 0)
#   a = line[0]
#   line[3] = 1 if line[3] == "-1"
#   for i in 1..3
#     a = a + " " + line[i].to_s
#   end
#   new_path = new_path = File.join(File.dirname(__FILE__), 'parsed_answer')
#   if line[3] != "-1"
#     open(new_path, 'a') { |f|
#       f.puts a
#     }
#   end
# end