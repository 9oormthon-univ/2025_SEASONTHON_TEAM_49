package org.chanme.be.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;

@Component
public class MemberCodeGenerator {

    private static final List<String> ANIMALS = List.of(
        "토끼","고양이","강아지","호랑이","사자","다람쥐","곰","늑대","수달","고래","양송이","양파","자라","치타",
        "참새","비둘기","독수리","부엉이","거북이","사슴","여우","판다","타조","하마","돌고래","국화","매화","오리","문어","너구리",
        "고라니","두더쥐","두루미","말","보리","더덕", "호박","토마토","사과","복숭아","수박","잉어","기린","나비","돼지"
    );

    private final SecureRandom random = new SecureRandom();

    /** 이름 정리(공백 제거, 한글 정규화) */
    public String normalizeName(String raw) {
        if (raw == null) return "";
        String s = raw.strip().replaceAll("\\s+", "");
        return Normalizer.normalize(s, Normalizer.Form.NFC);
    }

    /** 랜덤 동물 하나 */
    public String pickAnimal() {
        return ANIMALS.get(random.nextInt(ANIMALS.size()));
    }

    /** YY/MM/DD 2자리 포맷 */
    public String yy(LocalDate d) { return String.format("%02d", d.getYear() % 100); }
    public String mm(LocalDate d) { return String.format("%02d", d.getMonthValue()); }
    public String dd(LocalDate d) { return String.format("%02d", d.getDayOfMonth()); }

    /** 표시용 회원코드 후보 생성 (이름+동물+YY → MM → DD) */
    public String[] candidates(String name, String animal, LocalDate birth) {
        String n = normalizeName(name);
        if (birth == null) {
            // 생년월일이 없으면 YY/MM/DD 대신 "00"→"11"→"22" 같은 고정 시퀀스 사용(충돌시 단계적)
            return new String[] { n + animal + "00", n + animal + "11", n + animal + "22" };
        }
        return new String[] {
            n + animal + yy(birth),
            n + animal + mm(birth),
            n + animal + dd(birth)
        };
    }
}
